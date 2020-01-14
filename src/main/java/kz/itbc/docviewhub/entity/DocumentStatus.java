package kz.itbc.docviewhub.entity;

public class DocumentStatus {
    private int id_DocumentStatus;
    private String name;

    public DocumentStatus(int id_DocumentStatus, String name)
    {
        this.id_DocumentStatus = id_DocumentStatus;
        this.name = name;
    }

    public int getId_DocumentStatus() {
        return id_DocumentStatus;
    }

    public void setId_DocumentStatus(int id_DocumentStatus) {
        this.id_DocumentStatus = id_DocumentStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
