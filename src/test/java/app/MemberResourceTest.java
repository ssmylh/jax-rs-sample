package app;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class MemberResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(MemberResource.class);
    }

    @Test
    public void testGet() throws Exception {
        String id = "admin_id";
        WebTarget target = getWebTarget(id);
        Response res = target.request(MediaType.APPLICATION_JSON).get();

        assertThat(res.getStatus(), is(200));
        Member member = res.readEntity(Member.class);
        assertThat(member.id, is(id));
        assertThat(member.name, is("admin"));
        close(res);
    }

    @Test
    public void testPost() {
        Member member = new Member();
        member.name = "member2_name";
        Entity<Member> entity = Entity.entity(member, MediaType.APPLICATION_JSON);
        Response res = target("members").request(MediaType.APPLICATION_JSON).post(entity);

        assertThat(res.getStatus(), is(201));
        assertThat(res.getHeaderString(HttpHeaders.LOCATION), is(notNullValue()));
        Member resMember = res.readEntity(Member.class);
        assertThat(resMember.id, is(notNullValue()));
        assertThat(resMember.name, is(member.name));
        close(res);
    }

    @Test
    public void testPut() {
        String id = "member1_id";
        String name = "member1_name";
        Response putRes = putMember(new Member(id, name));
        assertThat(putRes.getStatus(), is(204));
        close(putRes);

        Response getRes = getWebTarget(id).request(MediaType.APPLICATION_JSON).get();
        Member member1 = getRes.readEntity(Member.class);
        assertThat(getRes.getStatus(), is(200));
        assertThat(member1.id, is(id));
        assertThat(member1.name, is(name));
        close(getRes);
    }

    @Test
    public void testDelete() {
        String id = "member2_id";
        String name = "member2_name";
        Response putRes = putMember(new Member(id, name));
        close(putRes);

        Response deleteRes = getWebTarget(id).request().delete();
        assertThat(deleteRes.getStatus(), is(204));
        close(deleteRes);

        Response getRes = getWebTarget(id).request().get();
        assertThat(getRes.getStatus(), is(404));
        close(getRes);
    }

    WebTarget getWebTarget(String id) {
        return target("members").path(id);
    }

    Response putMember(Member member) {
        Entity<Member> body = Entity.entity(member, MediaType.APPLICATION_JSON);
        return getWebTarget(member.id).request(MediaType.APPLICATION_JSON).put(body);
    }
}
