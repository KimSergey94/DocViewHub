package kz.itbc.docviewhub.entity;

public class Company {
    private int id;
    private String nameRU;
    private String nameKZ;
    private String bin;
    private String jsonPublicKey;
    private String serverAddress;
    private boolean isDeleted;

    public Company(int id, String nameRU, String nameKZ, String bin, String jsonPublicKey, String serverAddress, boolean isDeleted) {
        this.id = id;
        this.nameRU = nameRU;
        this.nameKZ = nameKZ;
        this.bin = bin;
        this.jsonPublicKey = jsonPublicKey;
        this.serverAddress = serverAddress;
        this.isDeleted = isDeleted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameRU() {
        return nameRU;
    }

    public void setNameRU(String nameRU) {
        this.nameRU = nameRU;
    }

    public String getNameKZ() {
        return nameKZ;
    }

    public void setNameKZ(String nameKZ) {
        this.nameKZ = nameKZ;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getJsonPublicKey() {
        return jsonPublicKey;
    }

    public void setJsonPublicKey(String jsonPublicKey) {
        this.jsonPublicKey = jsonPublicKey;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }
}
