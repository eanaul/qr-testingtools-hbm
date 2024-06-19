package dev.hbm.qris_testingtools.SpringLogic.NetworkConfig;

import dev.hbm.qris_testingtools.Enum.NetworkStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class NetworkConfigService {
    protected final NetworkConfigRepository networkConfigRepository;

    @Autowired
    public NetworkConfigService(NetworkConfigRepository networkConfigRepository) {
        this.networkConfigRepository = networkConfigRepository;
    }

    public List<NetworkConfig> fetchAll(){
        return networkConfigRepository.findAll();
    }

    public NetworkConfig findById(long id) {
        return validateId(id);
    }

    public List<NetworkConfig> fetchAllByScheme(long schemeId){
        return networkConfigRepository.findAllBySchemeConfig_IdOrderByIdAsc(schemeId);
    }

    public List<NetworkConfig> fetchAllBySchemeAndState(long schemeId, NetworkStateEnum state){
        return networkConfigRepository.findAllBySchemeConfig_IdAndStateOrderByIdAsc(schemeId, state);
    }

    public Optional<NetworkConfig> findByRemoteAndAddress(String addr, int port){
        return networkConfigRepository.findByLocalAddrAndLocalPort(addr, port);
    }

    public List<NetworkConfig> fetchServerConnectionBySchemeId(long schemeId) {
        return networkConfigRepository.findAllByStateAndSchemeConfig_Id(NetworkStateEnum.SERVER, schemeId);
    }

    public NetworkConfig fetchLocalConnection(String ip, int port) {
        return networkConfigRepository.findByLocalAddrAndLocalPort(ip, port)
                .orElseThrow(() -> new RuntimeException("Connection isn't found"));
    }

    public void add(NetworkConfig networkConfig) {
        if (networkConfig.getState() == NetworkStateEnum.CLIENT) {
            validateLocalAddrPort(0L, networkConfig.getRemoteAddr(), networkConfig.getRemotePort());
        } else if (networkConfig.getState() == NetworkStateEnum.SERVER) {
            validateLocalAddrPort(0L, networkConfig.getLocalAddr(), networkConfig.getLocalPort());
        }

        networkConfigRepository.save(networkConfig);
    }

    public void update(NetworkConfig networkConfig) {
        validateId(networkConfig.getId());

        if (networkConfig.getState() == NetworkStateEnum.CLIENT) {
            validateLocalAddrPort(0L, networkConfig.getRemoteAddr(), networkConfig.getRemotePort());
        } else if (networkConfig.getState() == NetworkStateEnum.SERVER) {
            validateLocalAddrPort(0L, networkConfig.getLocalAddr(), networkConfig.getLocalPort());
        }

        networkConfigRepository.save(networkConfig);
    }

    public void setConnectedNetwork(NetworkConfig networkConfig) {
        validateId(networkConfig.getId());
        networkConfigRepository.save(networkConfig);
    }

    protected NetworkConfig validateId(long id) {
        return networkConfigRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Existing data not found!"));
    }

    protected void validateLocalAddrPort(long id, String addr, int port) {
        this.networkConfigRepository.findByLocalAddrAndLocalPort(addr, port)
                .ifPresent(v1 -> {
                    if (v1.getId() != id)
                        throw new RuntimeException("Data already exist");
                });
    }

    protected void validateRemoteAddrPort(long id, String addr, int port) {
        this.networkConfigRepository.findByRemoteAddrAndRemotePort(addr, port)
                .ifPresent(v1 -> {
                    if (v1.getId() != id)
                        throw new RuntimeException("Data already exist");
                });
    }

    public void deleteById(long id){
        networkConfigRepository.deleteById(id);
    }

    public void validateLocalAddrAndLocalPort(NetworkConfig networkConfig) {
        Optional<NetworkConfig> existingNetworkConfig = networkConfigRepository.findByLocalAddrAndLocalPort(networkConfig.getLocalAddr(), networkConfig.getLocalPort());
        existingNetworkConfig.ifPresent(existing -> {
            if (Objects.equals(existing.getSchemeConfig().getId(), networkConfig.getSchemeConfig().getId())){
                throw new IllegalStateException("Local Address / Local Port already exist!");
            }
        });
    }

    public void validateRemoteAddrAndRemotePort(NetworkConfig networkConfig){
        Optional<NetworkConfig> existingNetworkConfig = networkConfigRepository.findByRemoteAddrAndRemotePort(networkConfig.getRemoteAddr(), networkConfig.getRemotePort());
        existingNetworkConfig.ifPresent(existing -> {
            if (Objects.equals(existing.getSchemeConfig().getId(), networkConfig.getSchemeConfig().getId())){
                throw new IllegalStateException("Remote Address / Remote Port already exist!");
            }
        });
    }
}
