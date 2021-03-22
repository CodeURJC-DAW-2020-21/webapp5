$(document).ready(function () {
	
	$("#loadMoreTournaments").click(function(){
	var nextPage = $(this).attr("nextPage");
	var tournamentPageId = nextPage - 1;

	var url = new String("/tournaments?page=" + nextPage.toString());
   	var loadUrl = new String(url + "#tournamentList");
    
	$.ajax({
   		url: "/tournaments/tournamentPages?pageId=" + tournamentPageId.toString(),
   		success: function(data) { 
   			if(data != "nomore"){   	        	
   	       		$("<div>").load(loadUrl, function() {
   	       			$("#tournamentList").append($(this).find("#tournamentList").html());
   	       		});
   	       		$("#loadMoreTournaments").attr("nextPage", Number(nextPage) + 1);
   			} else {
   				$("#loadMoreTournaments").prop("disabled", true);
           		$("#loadMoreTournaments").text("No More Results");
   			}
   		},
   		error: function() {
   	        console.log("No se ha podido obtener la información");
   	    }
    });
	
	})
})