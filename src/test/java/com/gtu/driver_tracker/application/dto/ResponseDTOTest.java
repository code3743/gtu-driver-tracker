package com.gtu.driver_tracker.application.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponseDTOTest {

    @Test
    void constructor_shouldSetAllFieldsCorrectly_withStringData() {
        String message = "Success";
        String data = "Driver created";
        int status = 201;

        ResponseDTO<String> dto = new ResponseDTO<>(message, data, status);

        assertEquals(message, dto.getMessage());
        assertEquals(data, dto.getData());
        assertEquals(status, dto.getStatus());
    }

    @Test
    void constructor_shouldSetAllFieldsCorrectly_withObjectData() {
        Dummy dummyData = new Dummy("abc", 123);
        ResponseDTO<Dummy> dto = new ResponseDTO<>("Success", dummyData, 200);

        assertEquals("Success", dto.getMessage());
        assertEquals(dummyData, dto.getData());
        assertEquals(200, dto.getStatus());
    }

    @Test
    void setters_shouldUpdateFieldsCorrectly() {
        ResponseDTO<Integer> dto = new ResponseDTO<>();
        dto.setMessage("Updated");
        dto.setData(42);
        dto.setStatus(202);

        assertEquals("Updated", dto.getMessage());
        assertEquals(42, dto.getData());
        assertEquals(202, dto.getStatus());
    }


    static class Dummy {
        private String code;
        private int number;

        public Dummy(String code, int number) {
            this.code = code;
            this.number = number;
        }

        public String getCode() {
            return code;
        }

        public int getNumber() {
            return number;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Dummy)) return false;
            Dummy d = (Dummy) o;
            return number == d.number && code.equals(d.code);
        }

        @Override
        public int hashCode() {
            return code.hashCode() * 31 + number;
        }
    }
}
