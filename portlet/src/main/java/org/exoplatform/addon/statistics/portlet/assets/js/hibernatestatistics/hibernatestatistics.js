require( ["SHARED/jquery", "SHARED/bootstrap", "SHARED/bts_tab", "SHARED/juzu-ajax" ], function ( $ )
{

$(document).ready(function() {
    $('#queries').DataTable();
    $('#collectionStatistics').DataTable();
    $('#entitiesStatistics').DataTable();
});
});
