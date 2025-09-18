package app.exceptions;


//-----Den arver fra RuntimeException → den er unchecked
//-----det er en speciel API excption
//---- god til at give fejlkoder som 404 eller andet
public class ApiException extends RuntimeException {
    private int code;

    public ApiException(int code, String msg){
        super(msg);
        this.code = code;
    }
    public int getCode(){
        return code;
    }
}
