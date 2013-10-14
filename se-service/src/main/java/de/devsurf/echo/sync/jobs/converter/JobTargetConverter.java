package de.devsurf.echo.sync.jobs.converter;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import de.devsurf.common.lang.converter.TwoWayConverter;
import de.devsurf.common.lang.formatter.ExceptionMessage;
import de.devsurf.echo.sync.api.Field;
import de.devsurf.echo.sync.jobs.api.JobTarget;
import de.devsurf.echo.sync.jobs.persistence.JobTargetEntity;
import de.devsurf.echo.sync.links.persistence.LinkEntity;
import de.devsurf.echo.sync.links.persistence.LinksPersistency;
import de.devsurf.echo.sync.persistence.FieldEntity;

public class JobTargetConverter implements TwoWayConverter<JobTargetEntity, JobTarget> {
	@Inject
	private TwoWayConverter<List<FieldEntity>, List<Field>> fieldConverter;

	@Inject
	private LinksPersistency links;

	@Override
	public JobTargetEntity convertFrom(JobTarget target) {
		if(target == null) {
			return null;
		}
		
		JobTargetEntity jobTarget = new JobTargetEntity();
		LinkEntity jobTargetLink = links.find(target.getLink());
		if (jobTargetLink == null) {
			throw new BadRequestException(ExceptionMessage.format(
					"link for target couldn't be found.").addParameter(
					"target", target.getLink()).build());
		}
		jobTarget.setLink(jobTargetLink);

		jobTarget.setFields(fieldConverter.convertFrom(target.getData()));
		
		return jobTarget;
	}

	@Override
	public JobTarget convertTo(final JobTargetEntity jobTarget) {
		if(jobTarget == null) {
			return null;
		}
		
		return new JobTarget() {
			private JobTargetEntity delegate = jobTarget;

			@Override
			public long getLink() {
				return delegate.getLink().getId();
			}

			@Override
			public List<Field> getData() {
				return fieldConverter.convertTo(delegate.getFields());
			}
		};
	}
}
