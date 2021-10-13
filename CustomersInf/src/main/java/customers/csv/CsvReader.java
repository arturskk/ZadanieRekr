package customers.csv;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.MappingStrategy;

import customers.pojo.CsvBean;
import customers.pojo.CsvTransfer;

public class CsvReader {

	public static List<CsvBean> beanBuilderExample(Path path, Class clazz) {
		ColumnPositionMappingStrategy ms = new ColumnPositionMappingStrategy();
		return beanBuilderExample(path, clazz, ms);
	}

	public static List<CsvBean> beanBuilderExample(Path path, Class clazz, MappingStrategy ms) {
		CsvTransfer csvTransfer = new CsvTransfer();
		try {
			ms.setType(clazz);

			Reader reader = Files.newBufferedReader(path);
			CsvToBean cb = new CsvToBeanBuilder(reader).withType(clazz)
					.withSkipLines(1)
					.withMappingStrategy(ms)
					.build();

			csvTransfer.setCsvList(cb.parse());
			reader.close();

		} catch (Exception ex) {
			//log error
		}
		return csvTransfer.getCsvList();
	}

}
