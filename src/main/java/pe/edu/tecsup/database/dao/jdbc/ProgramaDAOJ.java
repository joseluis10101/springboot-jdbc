package pe.edu.tecsup.database.dao.jdbc;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import pe.edu.tecsup.database.dao.ProgramaDAO;
import pe.edu.tecsup.database.helper.mappers.ProgramaRowMapper;
import pe.edu.tecsup.database.model.Programa;

@Repository
public class ProgramaDAOJ extends JdbcDaoSupport implements ProgramaDAO {

@Autowired
private DataSource dataSource;

@PostConstruct
private void initialize() {
    setDataSource(dataSource);
}

@Override
public List<Programa> list() {
    String sql = "select * from programa";
    return this.getJdbcTemplate().query(sql, new ProgramaRowMapper());
}

@Override
public Programa get(Long id) {
    String sql = "select * from programa where id = ?";

    // Parámetros del QueryForObjet:
    // (1)Query , (2)Array de Parámetros del Query y  (3) el RowMapper
    Programa programa = (Programa) this.getJdbcTemplate()
            .queryForObject(sql, new Object[]{id}, new ProgramaRowMapper());
    return programa;
}

@Override
public void save(Programa t) {
    String sql = "insert into programa(nombre,descripcion, codigo)values(?,?,?)";

    try {
        this.getJdbcTemplate().update(
                sql,
                new Object[]{
                    t.getNombre(),
                    t.getDescripcion(),
                    t.getCodigo()
                }
        );

        Long id = this.getJdbcTemplate().queryForObject("select last_insert_id()", Long.class);
        t.setId(id);

    } catch (DataAccessException e) {
        System.err.println("ERROR: " + e.getMessage());
    }
}

@Override
public void update(Programa t) {
    String sql = "update programa set nombre=?, descripcion=?, codigo=? where id=?";

    try {
        this.getJdbcTemplate().update(
                sql,
                new Object[]{
                    t.getNombre(),
                    t.getDescripcion(),
                    t.getCodigo(),
                    t.getId()
                }
        );
    } catch (DataAccessException e) {
        System.err.println("ERROR: " + e.getMessage());
    }
}

@Override
public void delete(Programa t) {
    String sql = "delete from programa where id=?";
    this.getJdbcTemplate().update(sql, new Object[]{t.getId()});
}

}
