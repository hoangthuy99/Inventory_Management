package com.ra.inventory_management.service.impl;

import com.ra.inventory_management.common.Constant;
import com.ra.inventory_management.model.dto.request.PurchaseOrderItemRequest;
import com.ra.inventory_management.model.dto.request.PurchaseOrderRequest;
import com.ra.inventory_management.model.dto.request.SearchRequest;
import com.ra.inventory_management.model.entity.*;
import com.ra.inventory_management.reponsitory.*;
import com.ra.inventory_management.service.PurchaseOrderService;
import com.ra.inventory_management.util.PageableUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PurchaseOrderServiceIMPL implements PurchaseOrderService {
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Override
    public PurchaseOrder createPurchaseOrder(PurchaseOrderRequest request) {
        log.info("start: createPurchaseOrder");

        // Get supplier by id if not found throw error
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found by id: " + request.getSupplierId()));

        // Get supplier by id if not found throw error
        Branch branch = branchRepository.findById(request.getBranchId().longValue())
                .orElseThrow(() -> new RuntimeException("Branch not found by id: " + request.getBranchId()));

        // Create instant for purchase order
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .orderDate(request.getOrderDate())
                .orderDatePlan(request.getOrderDatePlan())
                .supplier(supplier)
                .createdAt(LocalDateTime.now())
                .note(request.getNote())
                .status(Constant.GDR_PENDING)
                .branch(branch)
                .deleteFg(false)
                .code(String.format("PCO%s", LocalDateTime.now().format(formatter)))
                .build();

        // Create instant for purchase order item
        List<PurchaseOrderItem> items = new ArrayList<>();
        for (PurchaseOrderItemRequest itemRequest : request.getItems()) {
            // Get product by id if not found throw error
            ProductInfo productInfo = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found by id: " + itemRequest.getProductId()));


            PurchaseOrderItem item = PurchaseOrderItem.builder()
                    .purchaseOrder(purchaseOrder)
                    .createdAt(LocalDateTime.now())
                    .quantityActual(itemRequest.getQuantityActual())
                    .quantityPlan(itemRequest.getQuantityPlan())
                    .product(productInfo)
                    .stockType(itemRequest.getStockType())
                    .itemUnit(itemRequest.getItemUnit())
                    .unitPrice(productInfo.getPrice())
                    .area(
                            areaRepository.findById(itemRequest.getAreaId()).orElse(null)
                    )
                    .build();

            items.add(item);
        }


        // Assign item líst for purchase order
        purchaseOrder.setPurchaseOrderItems(items);
        purchaseOrder.setTotalAmount(items.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantityPlan())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        // Save purchase order
        PurchaseOrder response = purchaseOrderRepository.save(purchaseOrder);


        log.info("end: createPurchaseOrder");
        return response;
    }

    @Override
    public PurchaseOrder updatePurchaseOrder(PurchaseOrderRequest request) {
        log.info("start: updatePurchaseOrder");

        // Get purchase order by id if not found throw error
        PurchaseOrder order = purchaseOrderRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Purchase order not found by id: " + request.getId()));

        // Get supplier by id if not found throw error
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found by id: " + request.getSupplierId()));
        order.setSupplier(supplier);

        // Get supplier by id if not found throw error
        Branch branch = branchRepository.findById(request.getSupplierId().longValue())
                .orElseThrow(() -> new RuntimeException("Branch not found by id: " + request.getBranchId()));
        order.setBranch(branch);


        // Assign value for order
        order.setOrderDate(request.getOrderDate());
        order.setOrderDatePlan(request.getOrderDatePlan());
        order.setNote(request.getNote());
        order.setStatus(request.getStatus());
        order.setUpdatedAt(LocalDateTime.now());

        // Create instant for purchase order item
        List<PurchaseOrderItem> items = new ArrayList<>();

        // Remove remain order if can not found in request
        for (PurchaseOrderItem item : order.getPurchaseOrderItems()) {
            if (
                    !request.getItems().stream().anyMatch(i -> item.getId().equals(i.getId()))
            ) {
                item.setDeleteFg(true);
                items.add(item);
            }
        }

        // Create or modifed already order
        for (PurchaseOrderItemRequest itemRequest : request.getItems()) {
            // Get product by id if not found throw error
            ProductInfo productInfo = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found by id: " + itemRequest.getProductId()));


            PurchaseOrderItem item = PurchaseOrderItem.builder()
                    .id(itemRequest.getId())
                    .purchaseOrder(order)
                    .createdAt(LocalDateTime.now())
                    .quantityActual(itemRequest.getQuantityActual())
                    .quantityPlan(itemRequest.getQuantityPlan())
                    .product(productInfo)
                    .stockType(itemRequest.getStockType())
                    .itemUnit(itemRequest.getItemUnit())
                    .unitPrice(productInfo.getPrice())
                    .deleteFg(false)
                    .area(
                            areaRepository.findById(itemRequest.getAreaId()).orElse(null)
                    )
                    .build();

            if (item.getId() != null) {
                item.setUpdatedAt(LocalDateTime.now());
            } else {
                item.setCreatedAt(LocalDateTime.now());
            }

            items.add(item);
        }


        // Assign item líst for purchase order
        order.setPurchaseOrderItems(items);
        order.setTotalAmount(items.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantityPlan())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        // Save purchase order
        PurchaseOrder response = purchaseOrderRepository.save(order);

        // If status ís done plus stock of product
        if (request.getId() != null && request.getStatus().equals(Constant.GDR_DONE)) {
            for (PurchaseOrderItemRequest itemRequest : request.getItems()) {
                ProductInfo productInfo = productRepository.findById(itemRequest.getProductId()).orElse(null);

                if (productInfo != null) {
                    Integer quantityUpdated = itemRequest.getQuantityActual() + productInfo.getQty();
                    productInfo.setQty(quantityUpdated);
                    productRepository.save(productInfo);
                }
            }
        }

        // If status is cancel subtract stock of product
        if (request.getId() != null && request.getStatus().equals(Constant.GDR_CANCELED)) {
            for (PurchaseOrderItemRequest itemRequest : request.getItems()) {
                ProductInfo productInfo = productRepository.findById(itemRequest.getProductId()).orElse(null);

                if (productInfo != null) {
                    Integer quantityUpdated = productInfo.getQty() - itemRequest.getQuantityActual();
                    productInfo.setQty(quantityUpdated);
                    productRepository.save(productInfo);
                }
            }
        }

        log.info("end: updatePurchaseOrder");
        return response;
    }

    @Override
    public Page<PurchaseOrder> searchPurchaseOrder(SearchRequest request) {
        log.info("start: searchPurchaseOrder");

        Pageable pageable = PageableUtil.create(request.getPageNum(), request.getPageSize(), request.getSortBy(), request.getSortType());

        Page<PurchaseOrder> purchaseOrders = purchaseOrderRepository.searchPurchase(request.getSearchKey(), request.getStatus(), pageable);

        log.info("end: searchPurchaseOrder");

        return purchaseOrders;
    }

    @Override
    public void deletePurchase(int id) {
        log.info("start: searchPurchaseOrder");

        PurchaseOrder order = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase order not found by id: " + id));

        order.setDeleteFg(true);

        purchaseOrderRepository.save(order);

        log.info("end: searchPurchaseOrder");

    }


    @Override
    public PurchaseOrder getById(int id) {
        log.info("start: getById");

        PurchaseOrder order = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase order not found by id: " + id));

        log.info("end: getById");

        return order;
    }
}
