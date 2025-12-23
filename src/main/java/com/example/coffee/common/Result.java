package com.example.coffee.common;

import lombok.Generated;

public class Result {
   private Integer code;
   private String message;
   private Object data;

   public static Result success(String message, Object data) {
      Result result = new Result();
      result.setCode(0);
      result.setMessage(message);
      result.setData(data);
      return result;
   }

   public static Result success(Object data) {
      return success("成功", data);
   }

   public static Result error(String message) {
      Result result = new Result();
      result.setCode(1);
      result.setMessage(message);
      result.setData((Object)null);
      return result;
   }

   @Generated
   public Integer getCode() {
      return this.code;
   }

   @Generated
   public String getMessage() {
      return this.message;
   }

   @Generated
   public Object getData() {
      return this.data;
   }

   @Generated
   public void setCode(final Integer code) {
      this.code = code;
   }

   @Generated
   public void setMessage(final String message) {
      this.message = message;
   }

   @Generated
   public void setData(final Object data) {
      this.data = data;
   }

   @Generated
   public boolean equals(final Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Result)) {
         return false;
      } else {
         Result other = (Result)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label47: {
               Object this$code = this.getCode();
               Object other$code = other.getCode();
               if (this$code == null) {
                  if (other$code == null) {
                     break label47;
                  }
               } else if (this$code.equals(other$code)) {
                  break label47;
               }

               return false;
            }

            Object this$message = this.getMessage();
            Object other$message = other.getMessage();
            if (this$message == null) {
               if (other$message != null) {
                  return false;
               }
            } else if (!this$message.equals(other$message)) {
               return false;
            }

            Object this$data = this.getData();
            Object other$data = other.getData();
            if (this$data == null) {
               if (other$data != null) {
                  return false;
               }
            } else if (!this$data.equals(other$data)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return other instanceof Result;
   }

   @Generated
   public int hashCode() {
      int PRIME = 31;
      int result = 1;
      Object $code = this.getCode();
      result = result * 59 + ($code == null ? 43 : $code.hashCode());
      Object $message = this.getMessage();
      result = result * 59 + ($message == null ? 43 : $message.hashCode());
      Object $data = this.getData();
      result = result * 59 + ($data == null ? 43 : $data.hashCode());
      return result;
   }

   @Generated
   public String toString() {
      Integer var10000 = this.getCode();
      return "Result(code=" + var10000 + ", message=" + this.getMessage() + ", data=" + this.getData() + ")";
   }
}
