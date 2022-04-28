package springbook.user.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import springbook.user.dao.strategy.AddStatement;
import springbook.user.dao.strategy.DeleteAllStatement;
import springbook.user.dao.strategy.StatementStrategy;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;

public class UserDao {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }



//    public void setConnectionMaker(ConnectionMaker connectionMaker) {
//        this.connectionMaker = connectionMaker;
//    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        StatementStrategy strategy = new AddStatement(user);
        contextWithStatementStrategy(strategy);
    }

    // local class 버전
    public void local_add(final User user) throws SQLException
    {
        class AddStatement implements StatementStrategy
        {
            @Override
            public PreparedStatement makePreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement("insert into users(id, name, password) values(?,?,?)");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());
                return ps;
            }
        }

        StatementStrategy strategy = new AddStatement();
        contextWithStatementStrategy(strategy);
    }

    /**
     * Inner Anonymous class
     * @param user
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void anonymous_add(final User user) throws SQLException
    {
        contextWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement("insert into users(id, name, password) values(?,?,?)");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());
                return ps;
            }
        });
    }

    public User findById(String id) throws ClassNotFoundException, SQLException {
        Connection con = dataSource.getConnection();

        PreparedStatement ps = con.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();

        User user = new User();
        if(rs.next()) {
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }


        rs.close();
        ps.close();
        con.close();

        if(user.getId()==null) //id is pk!
            throw new EmptyResultDataAccessException(1);


        return user;
    }


    // 예외 발생해도 자원을 반환하게
    public void deleteAll() throws SQLException {
        StatementStrategy strategy = new DeleteAllStatement();
        contextWithStatementStrategy(strategy);
    }

    public void anonymous_deleteAll() throws SQLException
    {
        contextWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection con) throws SQLException {
                return con.prepareStatement("delete from users");
            }
        });
    }


    public void contextWithStatementStrategy(StatementStrategy strategy) throws SQLException
    {
        Connection connection = null;
        PreparedStatement ps = null;
        try{
            connection = dataSource.getConnection();

            ps = strategy.makePreparedStatement(connection);

            ps.executeUpdate();
        }catch (SQLException e)
        {
            throw e;
        }finally {
            if(ps!=null)
            {
                try{
                    ps.close();
                }catch (SQLException e)
                {

                }
            }

            if(connection!=null)
            {
                try{
                    connection.close();
                }catch (SQLException e)
                {

                }
            }
        }
    }





    public int getCount() throws SQLException {

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            connection = dataSource.getConnection();
            ps = connection.prepareStatement("select count(*) from users");
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }catch (SQLException e)
        {
            throw e;
        }finally {
            if(rs!=null)
            {
                try{
                    rs.close();
                }catch (SQLException e)
                {

                }
            }

            if(ps!=null)
            {
                try{
                    ps.close();
                }catch (SQLException e)
                {

                }
            }

            if(connection!=null)
            {
                try{
                    connection.close();
                }catch (SQLException e)
                {

                }
            }

        }



//        Connection connection = dataSource.getConnection();
//        PreparedStatement ps = connection.prepareStatement("select count(*) from users");
//
//        ResultSet res = ps.executeQuery();


    }




}
