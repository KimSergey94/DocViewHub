package kz.itbc.docviewhub.entity;

public class Company {
    private int id;
    private int idDocViewHub;
    private String nameRU;
    private String nameKZ;
    private String bin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdDocViewHub() {
        return idDocViewHub;
    }

    public void setIdDocViewHub(int idDocViewHub) {
        this.idDocViewHub = idDocViewHub;
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
}
