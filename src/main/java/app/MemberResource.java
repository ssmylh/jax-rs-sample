package app;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

@Path("members")
public class MemberResource {
    private static final ConcurrentHashMap<String, Member> members;
    private static final AtomicLong counter = new AtomicLong();
    static {
        members = new ConcurrentHashMap<>();
        members.put("admin_id", new Member("admin_id", "admin"));
    }

    @Context
    UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Member getMember(@PathParam("id") String id) {
        Member member = members.get(id);
        if (member == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        return member;
    }

    /**
     * Add a member.
     * @param member
     * @return The response with the status code and the location header.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postMember(Member member) {
        String id = String.valueOf(counter.incrementAndGet());
        Member copied = new Member(id, member.name);
        members.put(id, copied);

        URI location = uriInfo.getAbsolutePathBuilder().clone().path(id).build();
        return Response.created(location).entity(copied).build();
    }

    @PUT
    @Path("{id}")
    public void putMember(@PathParam("id") String id, Member member) {
        members.put(id, member);
    }

    @DELETE
    @Path("{id}")
    public void deleteMember(@PathParam("id") String id) {
        members.remove(id);
    }

}
