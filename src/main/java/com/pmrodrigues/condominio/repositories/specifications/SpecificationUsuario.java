package com.pmrodrigues.condominio.repositories.specifications;

import com.pmrodrigues.condominio.models.Usuario;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpecificationUsuario {
    public static Specification<Usuario> usuario(String login) {
        return (root, query, criteriaBuilder) ->{
            if( login != null && isNotBlank(login)) {
                return criteriaBuilder.equal(root.get("username"), login);
            }
            return null;
        };
    }

    public static Specification<Usuario> perfis(List<String> perfis) {
        return (root, query, criteriaBuilder) -> {
            if( perfis!=null && !perfis.isEmpty()) {
                val join = root.join("perfis", JoinType.INNER);
                return criteriaBuilder.in(join.get("authority")).value(perfis);
            }
            return null;
        };
    }
}
