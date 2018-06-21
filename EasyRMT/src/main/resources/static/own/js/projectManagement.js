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

function deleteFile(path, fileId) {
    $.ajax({
        url: path+'/file/'+fileId,
        type: 'DELETE',
        success: function() {
            $("#deleteFileModal").modal('hide');
            $("#deleteFileModalOk").modal('show');
        },
        error: function () {
            $("#deleteFileModalModal").modal('hide');
            $("#deleteFileModalFail").modal('show');
        }
    });
}

function deleteUser(user) {
    $.ajax({
        url: '/admin/users/'+user,
        type: 'DELETE',
        success: function() {
            $("#deleteUserModal").modal('hide');
            $("#deleteUserModalOk").modal('show');
        },
        error: function () {
            $("#deleteUserModal").modal('hide');
            $("#deleteUserModalFail").modal('show');
        }
    });
}

function deleteFromGroup(user, groupId) {
    $.ajax({
        url: '/admin/groups/group/'+groupId+'/user/'+user,
        type: 'DELETE',
        success: function() {
            $("#deleteFromGroupModal").modal('hide');
            $("#deleteFromGroupModalOk").modal('show');
        },
        error: function () {
            $("#deleteFromGroupModal").modal('hide');
            $("#deleteFromGroupModalFail").modal('show');
        }
    });
}

function deleteTrace(projectId, object1Id, object2Id) {
    $.ajax({
        url: '/traceability/'+projectId+'/obj1/'+object1Id+'/obj2/'+object2Id,
        type: 'DELETE',
        success: function() {
            $("#deleteTraceModal").modal('hide');
            $("#deleteTraceModalModalOk").modal('show');
        },
        error: function () {
            $("#deleteTraceModal").modal('hide');
            $("#deleteRequirementModalFail").modal('show');
        }
    });
}


function sendGroupForm(groupId) {
    $.ajax({
        type: "POST",
        url: "/admin/groups/group/"+groupId+"/1",
        data: $("#updateGroup1Form").serialize(), // serializes the form's elements.
        success: function(data)
        {
            $("#updateGroup1").modal('hide');
            $("#successUpdate").modal('show');
        },
        error: function () {
            $("#updateGroup1").modal('hide');
            $("#failUpdate").modal('show');
        }
    });
}

function sendForm(projectId, objectId) {
    $.ajax({
        type: "POST",
        url: '/'+projectId+'/'+objectId+'/addComment',
        data: $("#CreateCommentForm").serialize(), // serializes the form's elements.
        success: function(data)
        {
            $("#createCommentModal").modal('hide');
            $("#successCreationComment").modal('show');
        },
        error: function () {
            $("#createCommentModal").modal('hide');
            $("#failCreationComment").modal('show');
        }
    });
}
function updateForm() {
    var button = document.getElementById("update_comment_button");
    var projectId = button.getAttribute("value");
    var objectId = button.getAttribute("value1");
    var commentId = button.getAttribute("value2");
    $.ajax({
        type: "POST",
        url: '/'+projectId+'/'+objectId+'/update/'+commentId,
        data: $("#updateCommentForm").serialize(), // serializes the form's elements.
        success: function(data)
        {
            $("#updateCommentModal").modal('hide');
            $("#successUpdateComment").modal('show');
        },
        error: function () {
            $("#updateCommentModal").modal('hide');
            $("#failUpdateComment").modal('show');
        }
    });
}

function deleteComment() {
    var button = document.getElementById("delete_comment_button");
    var projectId = button.getAttribute("value");
    var objectId = button.getAttribute("value1");
    var commentId = button.getAttribute("value2");
    $.ajax({
        url: '/'+projectId+'/'+objectId+'/delete/'+commentId,
        type: 'DELETE',
        success: function() {
            $("#deleteCommentModal").modal('hide');
            $("#deleteCommentModalOk").modal('show');
        },
        error: function () {
            $("#deleteCommentModal").modal('hide');
            $("#deleteCommentModalFail").modal('show');
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
function modalValueExtended(id1, id2, id3){
    var button = document.getElementById("delete_comment_button");
    var att1 = document.createAttribute("value");
    att1.value = id1;
    var att2 = document.createAttribute("value1");
    att2.value = id2;
    var att3 = document.createAttribute("value2");
    att3.value = id3;
    button.setAttributeNode(att1);
    button.setAttributeNode(att2);
    button.setAttributeNode(att3);
}

function editComment(id1, id2, id3){
    var button = document.getElementById("update_comment_button");
    var att1 = document.createAttribute("value");
    att1.value = id1;
    var att2 = document.createAttribute("value1");
    att2.value = id2;
    var att3 = document.createAttribute("value2");
    att3.value = id3;
    button.setAttributeNode(att1);
    button.setAttributeNode(att2);
    button.setAttributeNode(att3);
    var textArea = document.getElementById("commentUpdate");
    var comment = document.getElementById("comment_"+id3);
    textArea.innerText = comment.innerText;
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

