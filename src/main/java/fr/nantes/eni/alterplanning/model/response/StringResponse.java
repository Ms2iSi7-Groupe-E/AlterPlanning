package fr.nantes.eni.alterplanning.model.response;

public class StringResponse {

    private String data;

    public StringResponse(String s) { 
       this.data = s;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}