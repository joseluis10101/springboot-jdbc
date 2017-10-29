package pe.edu.tecsup.database.dao.jdbc;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import pe.edu.tecsup.database.dao.CursoDAO;
import pe.edu.tecsup.database.helper.mappers.CursoRowMapper;
import pe.edu.tecsup.database.model.Curso;

@Repository
public class CursoDAOJ extends JdbcDaoSupport implements CursoDAO {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public List<Curso> list() {
        String sql = "select * from curso";
        return this.getJdbcTemplate().query(sql, new CursoRowMapper());
    }

    @Override
    public Curso get(Long id) {
        String sql = "select * from curso where id = ?";

        // Parámetros del QueryForObjet:
        // (1)Query , (2)Array de Parámetros del Query y  (3) el RowMapper
        try {
            
            Curso curso = (Curso) this.getJdbcTemplate()
                    .queryForObject(sql, new Object[]{id}, new CursoRowMapper());
            return curso;
            
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void save(Curso t) {
        String sql = "insert into curso(nombre,codigo)values(?,?)";

        try {
            this.getJdbcTemplate().update(
                    sql,
                    new Object[]{
                        t.getNombre(),
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
    public void update(Curso t) {
        String sql = "update curso set nombre=?, codigo=? where id=?";

        try {
            this.getJdbcTemplate().update(
                    sql,
                    new Object[]{
                        t.getNombre(),
                        t.getCodigo(),
                        t.getId()
                    }
            );
        } catch (DataAccessException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    @Override
    public void delete(Curso t) {
        String sql = "delete from curso where id=?";
        this.getJdbcTemplate().update(sql, new Object[]{t.getId()});
    }

}
