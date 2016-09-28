@Portlet
@Application(name = "HibernateStatistics")
@Bindings({@Binding(HibernateStatisticsService.class)})
@Scripts({
        @Script(id = "jquery-dataTables-js", value = "js/datatable/jquery.dataTables.min.js"),
        @Script(id = "datatable-js", value = "js/datatable/dataTables.bootstrap.min.js",depends = {"jquery-dataTables-js"}),
        @Script(id = "hibernate-statistics-js", value = "js/hibernatestatistics/hibernatestatistics.js",depends = {"datatable-js"})
           })
@Stylesheets({ @Stylesheet(id = "datatable-css", value = "css/datatable/dataTables.bootstrap.min.css"),
        @Stylesheet(id = "bootstrap-css", value = "css/datatable/bootstrap.min.css"),
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