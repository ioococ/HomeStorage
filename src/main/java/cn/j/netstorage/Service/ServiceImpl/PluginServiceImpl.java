package cn.j.netstorage.Service.ServiceImpl;

import cn.j.netstorage.Entity.plugin.Plugin;
import cn.j.netstorage.Mapper.HttpMapper;
import cn.j.netstorage.Service.PluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PluginServiceImpl implements PluginService {

    @Autowired
    private HttpMapper httpMapper;

    @Override
    public Boolean save(Plugin httpPlugin) {
        httpPlugin=httpMapper.save(httpPlugin);
        return httpPlugin.getId()!=0;
    }

    @Override
    public Boolean delete(Plugin httpPlugin){
        try {
            httpMapper.delete(httpPlugin);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Plugin> httpPlugins() {
        return httpMapper.findAll();
    }

    @Override
    public Plugin httpPlugin(Long id) {
        return httpMapper.findById(id).get();
    }

    @Override
    public Boolean update(Plugin httpPlugin) {
        return this.save(httpPlugin);
    }

    @Override
    public List<Plugin> searchPlugins(Plugin httpPlugin) {
        return httpMapper.findAllByPluginNameContaining(httpPlugin.getPluginName());
    }
}
