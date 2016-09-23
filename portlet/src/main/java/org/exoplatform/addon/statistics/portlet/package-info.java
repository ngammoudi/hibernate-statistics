@Portlet
@Application(name = "HibernateStatistics")
@Bindings({@Binding(HibernateStatisticsService.class)})
@Scripts({ @Script(id = "jquery", value = "js/jquery/jquery-1.12.3.js",location = AssetLocation.APPLICATION),
        @Script(id = "juzu", value = "js/common/juzuFunctions.js",location = AssetLocation.APPLICATION),
        @Script(id = "jquery.dataTables-js", value = "js/datatable/jquery.dataTables.min.js", location = AssetLocation.APPLICATION),
        @Script(id = "datatable-js", value = "js/datatable/dataTables.bootstrap.min.js", location = AssetLocation.APPLICATION,depends = {"jquery","jquery.dataTables-js"}),

           @Script(id = "hibernate-statistics-js", value = "js/hibernatestatistics/hibernatestatistics.js", location = AssetLocation.APPLICATION,depends = {"jquery","datatable-js"})
           })
@Stylesheets({ @Stylesheet(id = "datatable-css", value = "css/datatable/dataTables.bootstrap.min.css",location = AssetLocation.APPLICATION),
               @Stylesheet(id = "hibernate-statistics-css", value = "css/hibernatestatistics/hibernatestatistics.css",location = AssetLocation.APPLICATION)})
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