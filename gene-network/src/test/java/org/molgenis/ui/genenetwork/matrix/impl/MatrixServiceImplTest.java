package org.molgenis.ui.genenetwork.matrix.impl;

import org.molgenis.data.DataService;
import org.molgenis.data.Entity;
import org.molgenis.file.FileStore;
import org.molgenis.ui.genenetwork.matrix.meta.MatrixMetadata;
import org.molgenis.ui.genenetwork.matrix.model.Score;
import org.molgenis.util.ResourceUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class MatrixServiceImplTest
{

	private MatrixServiceImpl matrixServiceImpl;

	@BeforeTest
	private void setUp()
	{
		Entity entity = mock(Entity.class);

		when(entity.getString(MatrixMetadata.FILE_LOCATION))
				.thenReturn(ResourceUtils.getFile(getClass(), "/testmatrix.txt").getAbsolutePath());
		when(entity.getString(MatrixMetadata.SEPARATOR)).thenReturn("TAB");
		when(entity.getIdValue()).thenReturn("test");
		DataService dataService = mock(DataService.class);
		when(dataService.findOneById(MatrixMetadata.PACKAGE + "_" + MatrixMetadata.SIMPLE_NAME, "test"))
				.thenReturn(entity);

		FileStore fileStore = mock(FileStore.class);

		matrixServiceImpl = new MatrixServiceImpl(dataService, fileStore);
	}

	@Test
	public void getValueByIndexTest()
	{
		assertEquals(1.123, matrixServiceImpl.getValueByIndex("test", 1, 1));
	}

	@Test
	public void getValueByNamesTest()
	{
		List<Score> results = matrixServiceImpl.getValueByNames("test", "gene1,gene2", "hpo234,hpo123");

		assertTrue(results.contains(Score.createScore("hpo123", "gene1", 1.123)));
		assertTrue(results.contains(Score.createScore("hpo234", "gene1", 1.234)));
		assertTrue(results.contains(Score.createScore("hpo123", "gene2", 2.123)));
		assertTrue(results.contains(Score.createScore("hpo234", "gene2", 2.234)));
		assertEquals(results.size(), 4);
	}
}
