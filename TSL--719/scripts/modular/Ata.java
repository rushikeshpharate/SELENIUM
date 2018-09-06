package scripts.modular;

import org.testng.AssertJUnit;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Driver;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class Ata {

	// SoftAssert s_assert;
	WebDriver driver;

	// static int i = 1;

	@SuppressWarnings("deprecation")
	public static String[][] getExcelData(String fileName, String sheetName)
			throws IOException {
		String[][] arrayExcelData = null;
		Workbook wb = null;
		try {
			File file = new File(fileName);
			FileInputStream fs = new FileInputStream(file);
			// .xls
			if (fileName.substring(fileName.indexOf(".")).equals(".xlsx")) {

				// If it is xlsx file then create object of XSSFWorkbook class

				wb = new XSSFWorkbook(fs);
			} else if (fileName.substring(fileName.indexOf(".")).equals(".xls")) {
				// If it is xls file then create object of HSSFWorkbook class
				wb = new HSSFWorkbook(fs);
			}
			Sheet sh = wb.getSheet(sheetName);

			int totalNoOfRows = sh.getPhysicalNumberOfRows();
			int totalNoOfCols = sh.getRow(0).getPhysicalNumberOfCells();

			System.out.println("totalNoOfRows=" + totalNoOfRows + ","
					+ " totalNoOfCols=" + totalNoOfCols);
			arrayExcelData = new String[totalNoOfRows - 1][totalNoOfCols];
			for (int i = 1; i <= totalNoOfRows - 1; i++) {
				for (int j = 0; j <= totalNoOfCols - 1; j++) {
					sh.getRow(i).getCell(j).setCellType(1);
					arrayExcelData[i - 1][j] = sh.getRow(i).getCell(j)
							.getStringCellValue().toString();
				}
			}
		} catch (Exception e) {
			System.out.println("error in getExcelData()");
		}
		return arrayExcelData;
	}

	@BeforeTest
	public void setUp() throws Exception {

		System.out.println("*****************  1 *************************");
		System.setProperty("webdriver.chrome.driver",
				"test\\resources\\drivers\\chromedriver.exe");
		// driver = new FirefoxDriver();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(90, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		System.out.println("*****************  3 *************************");
		driver.get("http://ata123456789123456789.appspot.com/");
		System.out.println("*****************  3.1 *************************");

		java.util.Date date = new java.util.Date();
		System.out.println("\n\nExecution Log - Start Time - "
				+ new Timestamp(date.getTime()));
	}

	@DataProvider(name = "DP1")
	public Object[][] createData1() throws IOException {
		Object[][] retObjArr = getExcelData(
				"test\\resources\\data\\AtaDataDriven.xlsx", "AtaDataDriven");
		System.out.println("*****************  2 *************************");
		return (retObjArr);

	}

	@Test(dataProvider = "DP1")
	public void testDataProviderExample(String Operation, String Num1,
			String Num2, String Result) throws Exception {
		// s_assert = new SoftAssert();

		ATAPageObjectModel apom = new ATAPageObjectModel(driver);

		String res1 = "null";

		switch (Operation.toUpperCase()) {
		case "ADD":
			res1 = apom.add(Num1, Num2);
			System.out.println("Addition:" + res1);
			break;
		case "MUL":
			res1 = apom.multiply(Num1, Num2);
			System.out.println("Multiplication:" + res1);
			break;
		case "SQR":
			res1 = apom.sqr(Num1, Num2);
			System.out.println("SQR:" + res1);
			break;
		case "COMP":
			System.out.println("COMP:" + apom.comp(Num1, Num2));
			res1 = apom.comp(Num1, Num2);
			break;
		case "SQRSUB":
			res1 = apom.sqrSub(Num1, Num2);
			System.out.println("SQRSUB:" + res1);
			break;
		case "EUCLID(+)":
			res1 = apom.euclidPlus(Num1, Num2);
			System.out.println("EUCLIDPLUS:" + res1);
			break;
		case "EUCLID(-)":
			res1 = apom.euclidMinus(Num1, Num2);
			System.out.println("EUCLIDMINUS:" + res1);
			break;
		default:
			System.out.println("INVALID OPERATION !!");

		}

		assertEquals(res1, Result);

	}

	@AfterClass
	public void tearDown() {
		driver.quit();

		java.util.Date date = new java.util.Date();
		System.out.println("\n\nExecution Log - End Time - "
				+ new Timestamp(date.getTime()));
	}

	/*
	 * public boolean verifyTextPresent(String value) { boolean x =
	 * driver.getPageSource().contains(value); return x; }
	 * 
	 * private boolean isElementPresent(By locator) { try {
	 * driver.findElement(locator); return true; } catch (NoSuchElementException
	 * e) { return false; } }
	 */
}