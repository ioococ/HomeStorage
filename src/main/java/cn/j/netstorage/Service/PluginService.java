package cn.j.netstorage.Service;

import cn.j.netstorage.Entity.plugin.Plugin;

import java.util.List;

public interface PluginService {
    Boolean save(Plugin httpPlugin);

    Boolean delete(Plugin httpPlugin);

    List<Plugin> httpPlugins();

    Plugin httpPlugin(Long id);

    Boolean update(Plugin httpPlugin);

    List<Plugin> searchPlugins(Plugin httpPlugin);
}
