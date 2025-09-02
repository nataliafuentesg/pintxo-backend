package com.pintxo.repositories;
import com.pintxo.models.EventSpecial;
import com.pintxo.models.EventType;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventSpecialRepository extends JpaRepository<EventSpecial, Long> {

    @Query("""
      select e from EventSpecial e
      where (:type is null or e.type = :type)
        and (:q is null or :q = '' or
             lower(concat(coalesce(e.title,''),' ',coalesce(e.description,''))) like lower(concat('%', :q, '%')))
      order by e.displayOrder asc nulls last, e.id desc
    """)
    Page<EventSpecial> search(@Param("type") EventType type, @Param("q") String q, Pageable pageable);

    List<EventSpecial> findByActiveTrueOrderByDisplayOrderAscIdAsc();
}
