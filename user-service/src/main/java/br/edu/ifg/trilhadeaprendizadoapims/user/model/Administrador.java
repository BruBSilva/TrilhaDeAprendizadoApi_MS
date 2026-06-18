package br.edu.ifg.trilhadeaprendizadoapims.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "administradores")
@Getter
@Setter
@AllArgsConstructor
public class Administrador extends Usuario { }
