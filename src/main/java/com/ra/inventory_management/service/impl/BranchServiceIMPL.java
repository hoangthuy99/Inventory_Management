package com.ra.inventory_management.service.impl;

import com.ra.inventory_management.common.Constant;
import com.ra.inventory_management.model.entity.Branch;
import com.ra.inventory_management.model.entity.Categories;
import com.ra.inventory_management.reponsitory.BranchRepository;
import com.ra.inventory_management.service.BranchService;
import com.ra.inventory_management.util.ExcelUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BranchServiceIMPL implements BranchService {
    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private ExcelUtil excelUtil;

    @Override
    public List<Branch> getAll() {
        return branchRepository.findAll();
    }

    @Override
    public Branch save(Branch branch) {
        return branchRepository.save(branch);
    }

    @Override
    public Optional<Branch> findById(Long id) {
        return branchRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        branchRepository.deleteById(id);
    }

    @Override
    public List<Branch> getbyActiveFlag() {
        return null;
    }

    @Override
    public List<Branch> searchByName(String keyword) {
        return branchRepository.searchByName(keyword);
    }

    @Override
    public List<Branch> importExcel(MultipartFile file) throws IOException {
        if (!excelUtil.hasExcelFormat(file)) {
            throw new RuntimeException("Excel file wrong format");
        }
        try {
            Iterator rows = excelUtil.toIterator(file.getInputStream());
            List<Branch> brancheList = new ArrayList<>();

            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row currentRow = (Row) rows.next();

                Iterator<Cell> cellsInRow = currentRow.iterator();
                int cellIndex = 0;
                Branch branch = new Branch();

                while (cellsInRow.hasNext()) {

                    Cell currCell = cellsInRow.next();
                    switch (cellIndex) {
                        case 0:
                            branch.setBranchCode(currCell.getStringCellValue());
                            break;
                        case 1:
                            branch.setName(currCell.getStringCellValue());
                            break;
                        case 2:
                            branch.setAddress(currCell.getStringCellValue());
                            break;
                        case 3:
                            branch.setPhone(currCell.getStringCellValue());
                            break;
                    }

                    branch.setActiveFlag(1);
                    branch.setCreatedDate(LocalDateTime.now());

                    cellIndex++;
                }

                brancheList.add(branch);
            }

            return branchRepository.saveAll(brancheList);
        } catch (IOException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Dữ liệu bị trùng lặp");
        }
    }

    @Override
    public Map<String, String> getSampleExcel() throws IOException {
        try {
            // Get file from resource
            ClassPathResource pathResource = new ClassPathResource(Constant.BRANCH_SAMPLE);
            if (!pathResource.exists()) {
                throw new FileNotFoundException("Không tìm thấy file: " + pathResource.getPath());
            }

            // Copy file to temp folder
            Path tempFile = Files.createTempFile("sample_branch", ".xlsx");
            Files.copy(pathResource.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            // Convert base64
            byte[] fileContent = Files.readAllBytes(tempFile);
            String base64Encoded = Base64.encodeBase64String(fileContent);


            // Remove file temp after write
            Files.deleteIfExists(tempFile);

            Map<String, String> response = new HashMap<>();
            response.put("fileName", "sample_branch");
            response.put("base64", "data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64," + base64Encoded);

            return response;
        } catch (IOException e) {
            throw e;
        }
    }
}
