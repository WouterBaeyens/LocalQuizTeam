/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {
    $('#addMemberForm')
        
        //.formValidation({})
        .on('submit', function(e) {
            // Prevent form submission
            e.preventDefault();
           // var $form = $(e.target),fv    = $form.data('formValidation');

            // Use Ajax to submit form data
            $.ajax({
                url: $('#addMemberForm').attr('action'),
                type: 'POST',
                data: $('#addMemberForm').serialize(),
                success: function(result) {
                location.reload(true);
                },
                    error: function (jqXHR, textStatus, errorThrown) {
                        //var err = eval("(" + jqXHR.responseText + ")");
                        //alert(err);
                        $('#err_list').html(jqXHR.responseText+"\n");
                    }
            });
        });
});

// Post to the provided URL with the specified parameters.
function post(path, parameters) {
    alert('test');
    var form = $('<form></form>');

    form.attr("method", "post");
    form.attr("action", path);

    $.each(parameters, function (key, value) {
        var field = $('<input></input>');

        field.attr("type", "hidden");
        field.attr("name", key);
        field.attr("value", value);

        form.append(field);
    }); 
    }
    
function deleteMember(email){
    var encoded_email = encodeURIComponent(email);
    $.ajax({
        type: "DELETE",
        url: "/LocalQuizTeam/api/member/delete/" + encoded_email,
        success: function (msg) {
                location.reload(true);
        },
        error: function (jqXHR, textStatus, errorThrown) {
          $('#err_list').html(jqXHR.responseText);
        }
    });
}

function subscribeMember(email,quizId){
    var encoded_email = encodeURIComponent(email);
    var encoded_quizId = encodeURIComponent(quizId);
    $.ajax({
        type: "GET",
        url: "/LocalQuizTeam/api/quiz/" + encoded_quizId +"/subscribe/" + encoded_email,
        success: function (msg) {
                location.reload(true);
        },
        error: function (jqXHR, textStatus, errorThrown) {
          $('#err_list').html(jqXHR.responseText + "\n");
        }
    });
}

function unsubscribeMember(email,quizId){
    var encoded_email = encodeURIComponent(email);
    var encoded_quizId = encodeURIComponent(quizId);
    $.ajax({
        type: "GET",
        url: "/LocalQuizTeam/api/quiz/" + encoded_quizId +"/unsubscribe/" + encoded_email,
        success: function (msg) {
                location.reload(true);
        },
        error: function (jqXHR, textStatus, errorThrown) {
          $('#err_list').html(jqXHR.responseText);
        }
    });
}