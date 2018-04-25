function deleteProject(id) {
    $.ajax({
        url: '/project/'+id,
        type: 'DELETE',
        success: function() {
            $("#deleteProjectModal").modal('hide');
            $("#deleteProjectModalOk").modal('show');
        },
        error: function () {
            $("#deleteProjectModal").modal('hide');
            $("#deleteProjectModalFail").modal('show');
        }
    });
}
function deleteEpic(projectId, epicId) {
    $.ajax({
        url: '/project/'+projectId+'/epic/'+epicId,
        type: 'DELETE',
        success: function() {
            $("#deleteEpicModal").modal('hide');
            $("#deleteEpicModalOk").modal('show');
        },
        error: function () {
            $("#deleteEpicModal").modal('hide');
            $("#deleteEpicModalFail").modal('show');
        }
    });
}

function deleteFeature(projectId, featureId) {
    $.ajax({
        url: '/project/'+projectId+'/feature/'+featureId,
        type: 'DELETE',
        success: function() {
            $("#deleteFeatureModal").modal('hide');
            $("#deleteFeatureModalOk").modal('show');
        },
        error: function () {
            $("#deleteFeatureModal").modal('hide');
            $("#deleteFEatureModalFail").modal('show');
        }
    });
}

function deleteUserStory(projectId, epicId, userStoryId) {
    $.ajax({
        url: '/project/'+projectId+'/epic/'+epicId+'/userstory/'+userStoryId,
        type: 'DELETE',
        success: function() {
            $("#deleteUsModal").modal('hide');
            $("#deleteUsModalOk").modal('show');
        },
        error: function () {
            $("#deleteUsModal").modal('hide');
            $("#deleteUsModalFail").modal('show');
        }
    });
}

function deleteUseCase(projectId, featureId, useCaseId) {
    $.ajax({
        url: '/project/'+projectId+'/feature/'+featureId+'/usecase/'+useCaseId,
        type: 'DELETE',
        success: function() {
            $("#deleteUcModal").modal('hide');
            $("#deleteUcModalOk").modal('show');
        },
        error: function () {
            $("#deleteUcModal").modal('hide');
            $("#deleteUcModalFail").modal('show');
        }
    });
}

function deleteRequirement(projectId, requirementId, useCaseId) {
    $.ajax({
        url: '/project/'+projectId+'/requirement/'+requirementId,
        type: 'DELETE',
        success: function() {
            $("#deleteRequirementModal").modal('hide');
            $("#deleteRequirementModalOk").modal('show');
        },
        error: function () {
            $("#deleteRequirementModal").modal('hide');
            $("#deleteRequirementModalFail").modal('show');
        }
    });
}


function modalValue(id){
    var button = document.getElementById("delete_button");
    var att = document.createAttribute("value");
    att.value = id;
    button.setAttributeNode(att);
}
function modalValueExtended(id1, id2){
    var button = document.getElementById("delete_button");
    var dismiss = document.getElementById("modal_dismiss")
    var att1 = document.createAttribute("value1");
    att1.value = id1;
    var att2 = document.createAttribute("value2");
    att2.value = id2;
    var att3 = document.createAttribute("value");
    att3.value = id2;
    button.setAttributeNode(att1);
    button.setAttributeNode(att2);
    dismiss.setAttributeNode(att3);
}


function startTable() {
    var locale = navigator.language;
    //http://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/
    var url = "http://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/English.json";
    if(locale == 'es-ES'){
        url ="http://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Spanish.json"
    }
    $('#dataTable').DataTable({
        responsive: true,
        language: {
            url: url
        },
    });
}
