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

/*
$(document).on('click', '#sign-in', function (){
    var login = document.getElementById("login");
    var password = document.getElementById("password");
    if(login !== null){
        login = login.value;
        if(password !== null){
            password = password.value;
            $.ajax({
                type: "POST",
                url:"login",
                data: 'login='+login+'&password='+password,
                success: function(data) {
                    $("html").html(data);
                }
            });
        }
    }
});
*/
$(document).on('click', '#content-button-add-company', function(){
    window.location = "addcompany";
});
$(document).on('click', '#content-button-edit-company', function(){
    window.location = "editcompany";
});
$(document).on('click', '#content-button-delete-company', function(){
    window.location = "deletecompany";
});


