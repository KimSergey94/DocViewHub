package kz.itbc.docviewhub.entity;

import java.sql.Timestamp;

public class DocumentQueue {
    private int id_DocumentQueue;
    private Company senderCompany;
    private Company recipientCompany;
    private String jsonData;
    private Timestamp receiveDate;
    private Timestamp sendDate;
    private DocumentStatus documentStatus;

    public DocumentQueue(int id_DocumentQueue, Company senderCompany, Company recipientCompany, String jsonData,
                         Timestamp receiveDate, Timestamp sendDate, DocumentStatus documentStatus){
        this.id_DocumentQueue = id_DocumentQueue;
        this.setSenderCompany(senderCompany);
        this.setRecipientCompany(recipientCompany);
        this.jsonData = jsonData;
        this.receiveDate = receiveDate;
        this.sendDate = sendDate;
        this.documentStatus = documentStatus;
    }
    public DocumentQueue(){}

    public int getId_DocumentQueue() {
        return id_DocumentQueue;
    }

    public void setId_DocumentQueue(int id_DocumentQueue) {
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
