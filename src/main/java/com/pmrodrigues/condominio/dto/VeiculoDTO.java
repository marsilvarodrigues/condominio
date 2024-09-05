package com.pmrodrigues.condominio.dto;

import com.pmrodrigues.condominio.models.Veiculo;

public record VeiculoDTO(
        String guid,
        String modelo,
        String cor,
        String placa,
        String fabricante
) {
    public static VeiculoDTO fromVeiculo(Veiculo veiculo) {
        if( veiculo == null ) return null;
        return new VeiculoDTO(veiculo.getGuid(), veiculo.getModelo(), veiculo.getCor(), veiculo.getPlaca(), veiculo.getFabricante() );
    }
}
