package mdrt_test.repository;

import mdrt.openapi.model.DocumentDTO;
import mdrt.openapi.model.LogMDDTO;
import mdrt.openapi.model.SpecificationsDTO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Mapper
public interface CreateDocumentMapper {
    @Insert("""
            insert into docs.master
            (doc_number, doc_date, comment)
            values
            (#{docId}, #{docDate}, #{comment});
            """)
    void addMaster(String docId, LocalDate docDate, String comment);

    @Update("""
            update docs.master
            set doc_number = #{newDocId}, doc_date = #{docDate}, comment = #{comment}
            where doc_number = #{docId};
            """)
    void changeMaster(String docId, String newDocId, LocalDate docDate, String comment);

    @Delete("""
            delete from docs.master
            where doc_number = #{docId};
            """)
    void deleteMaster(String docId);

    @Select("""
            select count(*) from docs.master
            where doc_number = #{docId};
            """)
    Integer getDocumentById(String docId);

    @Insert("""
            insert into docs.detail
            (doc_number, item_name, item_amount)
            values
            (#{docId}, #{name}, #{ammount});
            """)
    void addDetail(String docId, String name, Integer ammount);

    @Update("""
            update docs.detail
            set item_name = #{newName}, item_amount = #{ammount}
            where doc_number = #{docId} and item_name = #{name};
            """)
    void changeDetail(String docId, String name, String newName, Integer ammount);

    @Delete("""
            delete from docs.detail
            where doc_number = #{docId} and item_name = #{name};
            """)
    void deleteDetail(String docId, String name);

    @Select("""
            select count(*) from docs.detail
            where doc_number = #{docId} and item_name = #{name};
            """)
    int getSpecificByName(String docId, String name);

    @Insert("""
            INSERT INTO docs.mdrt_log
            (table_name, success, message, record_data)
            VALUES
            (#{tableName}, #{errorCode}, #{message}, #{recordData})
            """)
    void documentLog(String tableName, Integer errorCode, String message, String recordData);

    @Select("""
            select master.doc_number docNumber,
            master.doc_date docDate,
            master.total_amount totalAmount,
            master."comment" Comment
            from docs.master;
            """)
    List<DocumentDTO> getMaster();

    @Select("""
            select detail.doc_number docNumber,
            detail.item_name name,
            detail.item_amount amount
            from docs.detail
            where detail.doc_number = #{docId};
            """)
    List<SpecificationsDTO> getDetail(String docId);

    @Select("""
            select mdrt_log.table_name tableName,
            mdrt_log.success succsess,
            mdrt_log.message message,
            mdrt_log."timestamp" timestamp,
            mdrt_log.record_data recordData
            from docs.mdrt_log
            order by id desc;
            """)
    List<LogMDDTO> getLog();
}
