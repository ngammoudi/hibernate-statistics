$(document).ready(function() {
    $('#queries').DataTable();
    $('#collectionStatistics').DataTable();
    $('#entitiesStatistics').DataTable();
} ));
function loadEntitiesStatistics(){
$("#entities").jzAjax({
        url : "HibernateStatisticsController.loadEntitiesStatistics()",
        })
        done(function(data) { {
         $("#entitiesStatistics").val(data.entitiesStatistics);
        }
    });
}
