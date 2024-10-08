package com.pmrodrigues.condominio.repositories.specifications;

import com.pmrodrigues.condominio.models.Apartamento;
import com.pmrodrigues.condominio.models.Morador;
import jakarta.persistence.criteria.JoinType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import org.springframework.data.jpa.domain.Specification;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpecificationMorador {
   public static Specification<Morador> nome(String nome) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if(!isBlank(nome) && !isEmpty(nome))
                return criteriaBuilder.like(root.get("nome"), "%" + nome + "%");
            else
                return null;
        };
    }

    public static Specification<Morador> cpf(String cpf) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if(!isBlank(cpf) && !isEmpty(cpf))
                return criteriaBuilder.equal(root.get("cpf"), cpf);
            else
                return null;
        };
    }

    public static Specification<Morador> apartamento(Apartamento apartamento) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if( apartamento != null ) {
                val join = root.join("apartamento", JoinType.INNER);
                return criteriaBuilder.equal(join.get("state"),apartamento);

            }else return null;
        };
    }
}
