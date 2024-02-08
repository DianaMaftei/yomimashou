package com.yomimashou.appscommon.model;

import com.yomimashou.appscommon.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Text extends Auditable<String> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(columnDefinition = "TEXT")
  private String title;

  @NotNull
  @Column(columnDefinition = "TEXT")
  private String content;

  @Column(columnDefinition = "TEXT")
  private String excerpt;

  private String imageFileName;

  @ElementCollection
  private List<String> tags;

  @ElementCollection
  private Map<String, Integer> kanjiCountByLevel;

  private int characterCount;

  @ElementCollection
  private Map<String, String> parsedWords;

  @Column(columnDefinition = "TEXT")
  private String parsedKanji;
}
