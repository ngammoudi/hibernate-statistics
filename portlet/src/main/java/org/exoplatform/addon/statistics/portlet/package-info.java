@Portlet
@Application(name = "HibernateStatistics")
@Bindings({@Binding(HibernateStatisticsService.class)})
@Scripts({@Script(id = "jquery", value = "js/jquery/jquery-1.12.3.js"),
        @Script(id = "datatable-js", value = "js/datatable/jquery.dataTables.min.js"),
        @Script(id = "datatable-buttons-js", value = "js/datatable/dataTables.buttons.min.js"),
        @Script(id = "flash-buttons-js", value = "js/datatable/buttons.flash.min.js"),
        @Script(id = "jszip-js", value = "js/datatable/jszip.min.js"),
        @Script(id = "pdfmake-js", value = "js/datatable/pdfmake.min.js"),
        @Script(id = "vfs-fonts-js", value = "js/datatable/vfs_fonts.js"),
        @Script(id = "html5-buttons-js", value = "js/buttons/buttons.html5.min.js"),
        @Script(id = "print-buttons-js", value = "js/buttons/buttons.print.min.js"),
        @Script(id = "hibernate-statistics-js", value = "js/hibernatestatistics/hibernatestatistics.js",depends = {"jquery","datatable-js"})
})
@Stylesheets({ @Stylesheet(id = "datatable-css", value = "css/datatable/jquery.dataTables.min.css"),
        @Stylesheet(id = "buttons-css", value = "css/datatable/buttons.dataTables.min.css"),
        @Stylesheet(id = "jquery-ui-css", value = "css/jquery/jquery-ui.css"),
        @Stylesheet(id = "hibernate-statistics-css", value = "css/hibernatestatistics/hibernatestatistics.css")})
@Assets("*")
package org.exoplatform.addon.statistics.portlet;

import juzu.Application;
import juzu.asset.AssetLocation;
import juzu.plugin.asset.Assets;
import juzu.plugin.asset.Script;
import juzu.plugin.asset.Scripts;
import juzu.plugin.asset.Stylesheet;
import juzu.plugin.asset.Stylesheets;
import juzu.plugin.binding.Binding;
import juzu.plugin.binding.Bindings;
import juzu.plugin.portlet.Portlet;
import org.exoplatform.addon.statistics.services.HibernateStatisticsService;