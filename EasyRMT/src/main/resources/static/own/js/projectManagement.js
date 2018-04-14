function deleteProject(id) {
    $.ajax({
        url: '/project/'+id,
        type: 'DELETE',
        success: function() {
            location.href("/dashboard");
        },
        error: function () {
            location.href("/500Error.html");
        }
    });

}
