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


function modalValue(id){
    var button = document.getElementById("delete_button");
    var att = document.createAttribute("value");
    att.value = id;
    button.setAttributeNode(att);
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
