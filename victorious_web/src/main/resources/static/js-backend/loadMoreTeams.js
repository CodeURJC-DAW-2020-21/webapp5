$(document).ready(function () {
	
	$("#loadMoreTeams").click(function(){
	var nextPage = $(this).attr("nextPage");
	var teamPageId = nextPage - 1;

	var url = new String("/teams?page=" + nextPage.toString());
   	var loadUrl = new String(url + "#teamList");
    
	$.ajax({
   		url: "/teams/teamPages?pageId=" + teamPageId.toString(),
   		success: function(data) { 
   			if(data != "nomore"){   	        	
   	       		$("<div>").load(loadUrl, function() {
   	       			$("#teamList").append($(this).find("#teamList").html());
   	       		});
   	       		$("#loadMoreTeams").attr("nextPage", Number(nextPage) + 1);
   			} else {
   				$("#loadMoreTeams").prop("disabled", true);
           		$("#loadMoreTeams").text("No More Results");
   			}
   		},
   		error: function() {
   	        console.log("No se ha podido obtener la información");
   	    }
    });
	
	})
})