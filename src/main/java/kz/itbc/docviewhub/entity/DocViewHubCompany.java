package kz.itbc.docviewhub.entity;

public class DocViewHubCompany extends Company {
    private String publicKeyBase64;
    private String serverAddress;
    private boolean isDeleted;

    public DocViewHubCompany(int idDocViewHub, String nameRU, String nameKZ, String bin, String publicKeyBase64, String serverAddress, boolean isDeleted) {
        this.setIdDocViewHub(idDocViewHub);
        this.setNameRU(nameRU);
        this.setNameKZ(nameKZ);
        this.setBin(bin);
        this.publicKeyBase64 = publicKeyBase64;
        this.serverAddress = serverAddress;
        this.isDeleted = isDeleted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
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
}
