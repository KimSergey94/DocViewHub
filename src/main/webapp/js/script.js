function getCompanyInfo(element) {
    var nameRU = document.getElementById("name-ru");
    var nameKZ = document.getElementById("name-kz");
    var bin = document.getElementById("bin");
    var govOrgNum = document.getElementById("gov-org-num");
    var serverAddress = document.getElementById("server-address");
    var delButton = document.getElementById("delete-button");
    var editButton = document.getElementById("edit-button");
    var company = document.getElementsByClassName("company-data-to-delete");
    var companyToEdit = document.getElementsByClassName("company-data-to-edit");
    var companyID = document.getElementById("companyID");
    $.ajax({
        type: "POST",
        url: "getcompanyinfo",
        data:'companyID=' + element.value,
        success: function(data){
            companyID.value = data.companyID;
            nameRU.value = data.name_ru;
            nameKZ.value = data.name_kz;
            bin.value = data.bin;
            govOrgNum.value = data.gov_org_num;
            serverAddress.value = data.server_address;
            if(delButton !== null){
                delButton.style.display = "block";
            }
            if(editButton !== null){
                editButton.style.display = "block";
            }
            for(var i=0; i<company.length; i++){
                company.item(i).style.display = "flex";
            }
            for(var i=0; i<companyToEdit.length; i++){
                companyToEdit.item(i).style.display = "flex";
            }
        }
    });
}

