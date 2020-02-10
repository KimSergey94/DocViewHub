package kz.itbc.docviewhub.entity;

import java.sql.Timestamp;

public class DocumentQueue {
    private long id_DocumentQueue;
    private Company senderCompany;
    private Company recipientCompany;
    private String jsonData;
    private Timestamp receiveDate;
    private Timestamp sendDate;
    private DocumentStatus documentStatus;
    private long id_ClientDocumentQueue;
    private boolean flagDeleted;
    private String aes;

    public DocumentQueue(){}

    public DocumentQueue(long id_DocumentQueue, Company senderCompany, Company recipientCompany, String jsonData,
                         Timestamp receiveDate, Timestamp sendDate, DocumentStatus documentStatus, long id_ClientDocumentQueue,
                         boolean flagDeleted, String aes){
        this.id_DocumentQueue = id_DocumentQueue;
        this.setSenderCompany(senderCompany);
        this.setRecipientCompany(recipientCompany);
        this.jsonData = jsonData;
        this.receiveDate = receiveDate;
        this.sendDate = sendDate;
        this.documentStatus = documentStatus;
        this.id_ClientDocumentQueue = id_ClientDocumentQueue;
        this.flagDeleted = flagDeleted;
        this.aes = aes;
    }

    public long getId_ClientDocumentQueue() {
        return id_ClientDocumentQueue;
    }

    public void setId_ClientDocumentQueue(long id_ClientDocumentQueue) {
        this.id_ClientDocumentQueue = id_ClientDocumentQueue;
    }

    public boolean isFlagDeleted() {
        return flagDeleted;
    }

    public void setFlagDeleted(boolean flagDeleted) {
        this.flagDeleted = flagDeleted;
    }

    public String getAes() {
        return aes;
    }

    public void setAes(String aes) {
        this.aes = aes;
    }

    public long getId_DocumentQueue() {
        return id_DocumentQueue;
    }

    public void setId_DocumentQueue(long id_DocumentQueue) {
        this.id_DocumentQueue = id_DocumentQueue;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public Timestamp getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Timestamp receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Timestamp getSendDate() {
        return sendDate;
    }

    public void setSendDate(Timestamp sendDate) {
        this.sendDate = sendDate;
    }

    public DocumentStatus getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(DocumentStatus documentStatus) {
        this.documentStatus = documentStatus;
    }

    public Company getSenderCompany() {
        return senderCompany;
    }

    public void setSenderCompany(Company senderCompany) {
        this.senderCompany = senderCompany;
    }

    public Company getRecipientCompany() {
        return recipientCompany;
    }

    public void setRecipientCompany(Company recipientCompany) {
        this.recipientCompany = recipientCompany;
    }
}
