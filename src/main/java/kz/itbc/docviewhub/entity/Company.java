package kz.itbc.docviewhub.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Company {
    @SerializedName("idDocViewHub")
    @Expose()
    private int id;
    @Expose()
    private String nameRU;
    @Expose()
    private String nameKZ;
    @Expose()
    private String bin;
    @Expose()
    private String govOrgNumber;
    @Expose()
    private String publicKeyBase64;
    @Expose(serialize = false, deserialize = false)
    private String serverAddress;
    @Expose()
    private boolean isDeleted;

    public Company(String nameRU, String nameKZ, String bin, String govOrgNumber, String serverAddress) {
        this.nameRU = nameRU;
        this.nameKZ = nameKZ;
        this.bin = bin;
        this.govOrgNumber = govOrgNumber;
        this.serverAddress = serverAddress;
    }

    public Company(int id, String nameRU, String nameKZ, String bin, String govOrgNumber, String publicKeyBase64, String serverAddress, boolean isDeleted) {
        this.id = id;
        this.nameRU = nameRU;
        this.nameKZ = nameKZ;
        this.bin = bin;
        this.govOrgNumber = govOrgNumber;
        this.publicKeyBase64 = publicKeyBase64;
        this.serverAddress = serverAddress;
        this.isDeleted = isDeleted;
    }

    public Company(){}

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

    public String getGovOrgNumber() {
        return govOrgNumber;
    }

    public void setGovOrgNumber(String govOrgNumber) {
        this.govOrgNumber = govOrgNumber;
    }

    public String getPublicKeyBase64() {
        return publicKeyBase64;
    }

    public void setPublicKeyBase64(String publicKeyBase64) {
        this.publicKeyBase64 = publicKeyBase64;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
