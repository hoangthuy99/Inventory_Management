package com.ra.inventory_management.model.dto.response;

import lombok.Data;

@Data
public class BaseResponse<T> {
        private int code = 200;
        private String message = "Success";
        private T data;

        public BaseResponse(T data){
            this.data = data;
        }

        public BaseResponse(int code, String message){
            this.code = code;
            this.message = message;
        }
}
