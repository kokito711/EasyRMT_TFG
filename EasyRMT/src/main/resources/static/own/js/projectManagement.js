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
