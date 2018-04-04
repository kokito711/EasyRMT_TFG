function validateForm() {
    var data = $('#create-project-form').serializeArray();
    var jData = jsonizeProject(data);
    sendData(jData);
}

/**
 * Make a post request to send the project info to the server to be saved
 * @param jData --> JSON valid string with project information
 */
function sendData(jData){
    //.preventDefault();
    $.ajax({
        type: "post",
        url: url + "/projects",
        contentType: "application/json; charset=utf-8",
        traditional: true,
        dataType: "json",
        async: false,
        data: jData,
        success: function (data) {
            populateProjects(data);
            includeHTML('/pages/project');
            //createProjectView(data);
        },
        error: function (data) {
            general_error();
        }
    });
    return false;
};

/**
 * This function parses the project form to a valid JSON
 * @param project
 * @returns {string} --> String with valid JSON
 */
function jsonizeProject(project) {//serialize project data
    var jProject = "{";
    for (var i = 0; i < project.length; i++){
        switch (project[i]['name']){
            case 'name': {
                jProject += '"name":"'+project[i]['value']+'",';
                break;
            }
            case 'description': {
                jProject += '"description" :"'+project[i]['value']+'",';
                break;
            }
            case 'projecttype': {
                jProject += '"projecttype" :"'+project[i]['value']+'"';
                break;
            }
            case 'idtype': {
                if(project[i-1]['name']!=="idtype"){
                    jProject += ', "requirementtypes": [{"idtype" : '+project[i]['value']+'},';
                    break;
                }
                else if(project[i+1]==null){
                    jProject +='{ "idtype" : '+project[i]['value']+'}]';
                    break;
                }
                else{
                    jProject += '{ "idtype" : '+project[i]['value']+'},';
                    break;
                }
            }
        }
    }
    return jProject+'}';
}

/**
 * This function loads and populates the page project.html with project info
 * @param data --> project data received from create project success request or load project success request
 * @param projectId --> projectId to be found in a request
 */
function createProjectView(data,projectId){
    includeHTML('/pages/project');
    $("#delete-project").removeClass("hidden");
    $("#update-project").removeClass("hidden");
    if(typeof projectId === "undefined") {
        if (typeof data === "undefined") {
            general_error();
        }
        else {
            $("#project-template-name").text(function(){
                return data.name;
            });
            if(data.projecttype === 'AGILE'){
                $("#feat-epic-panel").text("Epics");
                $("#feat-epic-panel").attr("onClick", "loadEpics('"+data.projectId+"')");
            }
            else {
                $("#feat-epic-panel").text("Features");
                $("#feat-epic-panel").attr("onClick", "loadFeatures('"+data.projectId+"')");
            }
        }
    }
    else {
        //call a request to get the project information
    }
    return false;
}
