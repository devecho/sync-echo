package de.devsurf.echo.sync.jobs.converter;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import de.devsurf.common.lang.formatter.ExceptionMessage;
import de.devsurf.echo.frameworks.rs.api.TwoWayConverter;
import de.devsurf.echo.sync.api.Field;
import de.devsurf.echo.sync.jobs.api.JobSource;
import de.devsurf.echo.sync.jobs.persistence.JobTargetEntity;
import de.devsurf.echo.sync.links.persistence.LinkEntity;
import de.devsurf.echo.sync.links.persistence.LinksPersistency;
import de.devsurf.echo.sync.persistence.FieldEntity;

public class JobSourceConverter implements TwoWayConverter<JobTargetEntity, JobSource> {
	@Inject
	private TwoWayConverter<List<FieldEntity>, List<Field>> fieldConverter;
	
	@Inject
	private LinksPersistency links;

	@Override
	public JobTargetEntity convertFrom(JobSource target) {
		if(target == null) {
			return null;
		}
		
		JobTargetEntity jobSource = new JobTargetEntity();
		LinkEntity jobSourceLink = links.find(target.getLink());
		if (jobSourceLink == null) {
			throw new BadRequestException(ExceptionMessage.format(
					"link for target couldn't be found.").addParameter(
					"target", target.getLink()).build());
		}
		jobSource.setLink(jobSourceLink);

		jobSourceLink.setFields(fieldConverter.convertFrom(target.getData()));
		
		return jobSource;
	}

	@Override
	public JobSource convertTo(final JobTargetEntity jobSource) {
		if(jobSource == null) {
			return null;
		}
		
		return new JobSource() {
			private JobTargetEntity delegate = jobSource;

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
