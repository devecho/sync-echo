package de.devsurf.echo.sync.jobs.converter;

import java.util.List;

import javax.inject.Inject;

import de.devsurf.common.lang.converter.TwoWayConverter;
import de.devsurf.echo.sync.api.Field;
import de.devsurf.echo.sync.jobs.api.Job;
import de.devsurf.echo.sync.jobs.api.JobSource;
import de.devsurf.echo.sync.jobs.api.JobTarget;
import de.devsurf.echo.sync.jobs.persistence.JobEntity;
import de.devsurf.echo.sync.jobs.persistence.JobTargetEntity;
import de.devsurf.echo.sync.links.persistence.LinksPersistency;
import de.devsurf.echo.sync.persistence.FieldEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderPersistency;

public class JobConverter implements TwoWayConverter<JobEntity, Job> {
	@Inject
	private ProviderPersistency providers;

	@Inject
	private LinksPersistency links;

	@Inject
	private TwoWayConverter<JobTargetEntity, JobTarget> targetConverter;

	@Inject
	private TwoWayConverter<JobTargetEntity, JobSource> sourceConverter;
	
	@Inject
	private TwoWayConverter<List<FieldEntity>, List<Field>> fieldConverter;

	@Override
	public JobEntity convertFrom(Job job) {
		JobEntity jobEntity = new JobEntity();
		if (job.getId() != 0) {
			jobEntity.setId(job.getId());
		}

		jobEntity.setUser(1);// TODO change to user object and make link
								// converter aware of context

		jobEntity.setName(job.getName());
		jobEntity.setDescription(job.getDescription());
		jobEntity.setEnabled(job.isEnabled());

		jobEntity.setSource(sourceConverter.convertFrom(job.getSource()));
		jobEntity.setTarget(targetConverter.convertFrom(job.getTarget()));

		jobEntity.setFields(fieldConverter.convertFrom(job.getData()));

		return jobEntity;
	}

	@Override
	public Job convertTo(final JobEntity source) {
		return new Job() {
			private JobEntity delegate = source;

			@Override
			public long getId() {
				return delegate.getId();
			}

			@Override
			public String getName() {
				return delegate.getName();
			}

			@Override
			public String getDescription() {
				return delegate.getDescription();
			}

			@Override
			public boolean isEnabled() {
				return delegate.isEnabled();
			}

			@Override
			public JobSource getSource() {
				return sourceConverter.convertTo(source.getSource());
			}

			@Override
			public JobTarget getTarget() {
				return targetConverter.convertTo(source.getTarget());
			}

			@Override
			public List<Field> getData() {
				return fieldConverter.convertTo(delegate.getFields());
			}
		};
	}
}
