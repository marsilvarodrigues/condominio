import {format} from "date-fns";

export const formatarData = (dataRecebida) => {
    const dataFormatada = format(new Date(dataRecebida), 'dd-MM-yyyy');
    return dataFormatada;
}

export const removeEmptyParams = (params) => {
    const searchParams = new URLSearchParams();

    // Itera sobre as chaves do objeto e adiciona apenas as que têm valores válidos
    Object.entries(params).forEach(([key, value]) => {
        if (value !== null && value !== undefined && value !== '' && !(Array.isArray(value) && value.length === 0)) {
            searchParams.append(key, value);
        }
    });

    return searchParams;
};