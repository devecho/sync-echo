package de.devsurf.echo.sync.jobs;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import de.devsurf.echo.frameworks.rs.api.Publishable.AbstractEndpoint;
import de.devsurf.echo.frameworks.rs.api.TwoWayConverter;
import de.devsurf.echo.sync.Resources.ResourcePath;
import de.devsurf.echo.sync.errors.ErrorResponse;
import de.devsurf.echo.sync.jobs.api.Job;
import de.devsurf.echo.sync.jobs.api.JobSource;
import de.devsurf.echo.sync.jobs.api.JobTarget;
import de.devsurf.echo.sync.jobs.persistence.JobEntity;
import de.devsurf.echo.sync.jobs.persistence.JobPersistency;
import de.devsurf.echo.sync.jobs.persistence.JobTargetEntity;
import de.devsurf.echo.sync.persistence.ItemAlreadyExistsException;


@Path(ResourcePath.JOBS_PATH)
public class JobResource extends AbstractEndpoint {

	@Inject
	private JobPersistency persistence;

	@Inject
	private TwoWayConverter<JobEntity, Job> converter;
	
	@Inject
	private TwoWayConverter<JobTargetEntity, JobTarget> targetConverter;

	@Inject
	private TwoWayConverter<JobTargetEntity, JobSource> sourceConverter;

	@Override
	public String description() {
		return "Endpoint returns the information about the authenticated user.";
	}

	@Override
	@GET
	@Consumes("*/*")
	@Produces("application/json")
	public Response get() {
		List<JobEntity> entities = persistence.findAll();
		List<Job> jobs = new ArrayList<>(entities.size());
		for (JobEntity entity : entities) {
			jobs.add(converter.convertTo(entity));
		}
		return Response.ok(new GenericEntity<List<Job>>(jobs) {
		}).build();
	}

	/**
	 * Returns the requested link is available for the user.
	 */
	@HEAD
	@Path("{jobId}")
	public Response isAvailable(@PathParam("jobId") String jobId) {
		findJob(jobId);
		return Response.ok().build();
	}

	/**
	 * Returns the requested provider, if available.
	 */
	@GET
	@Path("{jobId}")
	public Response find(@PathParam("jobId") String jobId) {
		JobEntity result = findJob(jobId);
		return Response.ok(converter.convertTo(result)).build();
	}
	
	@GET
	@Path("{jobId}/source")
	public Response getSource(@PathParam("jobId") String jobId) {
		JobEntity result = findJob(jobId);
		Job job = converter.convertTo(result);
		return Response.ok(job.getSource()).build();
	}
	
	@GET
	@Path("{jobId}/target")
	public Response getTarget(@PathParam("jobId") String jobId) {
		JobEntity result = findJob(jobId);
		Job job = converter.convertTo(result);
		return Response.ok(job.getTarget()).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Job job) throws ItemAlreadyExistsException {
		validateJob(job);

		JobEntity target = converter.convertFrom(job);
		long id = persistence.persist(target).getId();

		return Response
				.created(
						UriBuilder.fromResource(getClass())
								.path(Long.toString(id)).build())
				.entity(converter.convertTo(target)).build();
	}

	@PUT
	@Path("{jobId}")
	public Response update(@PathParam("jobId") String jobId, Job job)
			throws ItemAlreadyExistsException {
		JobEntity result = findJob(jobId);

		validateJob(job);

		JobEntity target = converter.convertFrom(job);
		if (target.getId() != result.getId()) {
			throw new BadRequestException(ErrorResponse.item("id")
					.withId(target.getId()).forStatus(Status.BAD_REQUEST));
		}
		persistence.merge(target);

		return Response.ok(converter.convertTo(target)).build();
	}
	
	@PUT
	@Path("{jobId}/source")
	public Response updateSource(@PathParam("jobId") String jobId, JobSource source)
			throws ItemAlreadyExistsException {
		JobEntity result = findJob(jobId);

		validateSource(source);
		JobTargetEntity jobSource = sourceConverter.convertFrom(source);
		result.setSource(jobSource);

		persistence.merge(result);

		return Response.ok(sourceConverter.convertTo(result.getSource())).build();
	}
	
	@PUT
	@Path("{jobId}/target")
	public Response updateTarget(@PathParam("jobId") String jobId, JobTarget target)
			throws ItemAlreadyExistsException {
		JobEntity result = findJob(jobId);

		validateSource(target);
		JobTargetEntity jobTarget = targetConverter.convertFrom(target);
		
		result.setTarget(jobTarget);

		persistence.merge(result);

		return Response.ok(targetConverter.convertTo(jobTarget)).build();
	}

	@DELETE
	@Path("{jobId}")
	public Response delete(@PathParam("jobId") String jobId) {
		JobEntity result = findJob(jobId);
		persistence.delete(result);
		return Response.ok().build();
	}
	
	private void validateJob(Job job) {
		//TODO validate if source or target exists
	}
	
	private void validateSource(JobSource source) {
		//TODO validate if source or target exists
	}
	
	private void validateSource(JobTarget source) {
		//TODO validate if source or target exists
	}
	
	private JobEntity findJob(String linkId) {
		long id;
		try {
			id = Long.parseLong(linkId);
		} catch (NumberFormatException e) {
			throw new WebApplicationException(ErrorResponse.item("jobs").withId(linkId).wasNotFound());
		}

		JobEntity result = persistence.find(id);
		if (result == null) {
			throw new WebApplicationException(ErrorResponse.item("jobs").withId(linkId).wasNotFound());
		}
		return result;
	}
}