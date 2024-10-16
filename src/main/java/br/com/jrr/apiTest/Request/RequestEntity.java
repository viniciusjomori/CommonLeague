package br.com.jrr.apiTest.Request;

import java.time.LocalDateTime;

import br.com.jrr.apiTest.App.BaseEntity;
import br.com.jrr.apiTest.Request.Enum.RequestMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "requests")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RequestEntity extends BaseEntity {

    @Column(nullable = false)
    private String endpoint;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestMethod method;
    
    @Column
    private String body;
    
    @Column
    private int httpStatus;

    @Column
    private LocalDateTime responseDate;

    @Column
    private String response;

}
