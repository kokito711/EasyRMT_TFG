/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

function uploadFile(path){
    $.ajax({
        url: path,
        data : $('#upload'),
        //contentType: 'multipart/form-data',
        processData: false,  // Important!
        //contentType: false,
        cache: false,
        type: 'POST',
        success: function() {
            location.reload();
        },
        progress: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $('#progress .bar').css(
                'width',
                progress + '%'
            );
        },
        error: function () {
            location.reload();
        }
    });
}
/*
$(function () {
    $('#fileupload').ondrop({
        dataType: 'multipart/form-data',

        done: function (e, data) {
            $.each(data.result, function (index, file) {
                location.reload();
            });
        },
        progress: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $('#progress .bar').css(
                'width',
                progress + '%'
            );
        },

        dropZone: $('#dropzone')
    });
});*/
