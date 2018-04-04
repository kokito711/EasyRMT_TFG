var url = "http://localhost:8080";

/**
 * This function is executed when the main page is loaded, then call
 * @link populateProjects() function
 */
$(document).ready(function() {
   populateProjects();
});

/**
 * shows a label with an general error
 */
function general_error(){
    $("#general_error").removeClass("hidden");
}

/**
 * Includes html content into the page making a request to the server
 * @param page
 */
function includeHTML(page) {
      page = page +".html"
      $("#content").load(page);
}

/**
 * Make a request to the server and populates via AJAX the list of existing projects
 * @param data --> if is not empty, add an element form received data, else makes the request
 */
function populateProjects(data){
    if(typeof data === "undefined") {
        $.ajax({
            type: "GET",
            url: url+"/projects"
        })
        .then(function(data, status, xhr) {
            var status = xhr['status'];
            var ul = $("#project-nav");
            switch (status) {
                case 200:
                    if (data.length == 0) {
                        $("#project_empty").removeClass("hidden");
                    }
                    else{
                        $.each(data, function(f) {
                            ul.append('<li><a href="#" onclick="createProjectView(\'projectId:\''+this.idproject+'\')">' + this.name + '</a></li>')
                        });

                    }
                    break;
                default:
                    general_error();

            }
        });
    }
    else {
        var ul = $("#project-nav");
        $.each(data, function(f) {
            ul.append('<li><a  href="#" onclick="createProjectView(\'projectId:\''+this.idproject+'\')">' + this.name + '</a></li>')
        });
    }


}