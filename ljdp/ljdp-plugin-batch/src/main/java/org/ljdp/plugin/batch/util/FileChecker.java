package org.ljdp.plugin.batch.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WriteException;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.bean.DynamicField;
import org.ljdp.common.file.JxlUtil;
import org.ljdp.common.file.TxtXlsResultFile;
import org.ljdp.plugin.batch.check.CheckFormat;
import org.ljdp.plugin.batch.check.FormatException;

public class FileChecker {
	
	public static TxtXlsResultFile checkTxtFile(File file, CheckFormat iCheckFormat)
			throws FormatException, WriteException {
		return checkTxtFile(file, iCheckFormat, null);
	}
    

    /**
     * checkFile 检查文件格式
     * @throws WriteException 
     */
    public static TxtXlsResultFile checkTxtFile(File file, CheckFormat iCheckFormat, ArrayList<DynamicField> checkFields)
            throws FormatException, WriteException {
        if ( iCheckFormat == null ) {
            return null;
        }
        int count = 0; //需处理的行数（如空行和标题不需处理）
        int currentRow = 0;// 记录当前检查到的行数
        RandomAccessFile rin = null;
        String errName = file.getAbsolutePath().replaceFirst(".txt", "_format.txt");
        TxtXlsResultFile resFile = new TxtXlsResultFile();
        resFile.initialize(errName);
        try {
        	resFile.openFile();
            rin = new RandomAccessFile(file, "r");
            long length = rin.length(); // 文件长度
            String line;
            if ( length <= 0 ) {
                throw new FormatException("上传数据为空!");
            }
         // 文件游标
            for (long filePointer = rin.getFilePointer(); filePointer < length; filePointer = rin.getFilePointer()) {
            	line = rin.readLine();
            	++currentRow;
            	iCheckFormat.setCurrentRow(currentRow);
            	if(currentRow < iCheckFormat.getBeginIndex()) {
            		continue;
            	}
            	if(StringUtils.isBlank(line)) {
            		continue;
            	}
                line = new String(line.getBytes("ISO-8859-1"), "GBK");
                try {
                	if(null == checkFields) {
                		iCheckFormat.checkLine(line);
                	} else {
                		iCheckFormat.checkLine(line, checkFields);
                	}
                	++count;
                	iCheckFormat.setPassCount(count);
                } catch (FormatException ex) {
                	resFile.writeErrorRecord(line + "|第" + currentRow + "行："
                            + ex.getMessage());
                }
            }
        } catch(FileNotFoundException ffe) {
            throw new FormatException("找不到文件：" + file.getName());
        } catch (IOException ioe) {
            throw new FormatException(ioe.getMessage());
        } finally {
            if(rin != null) {
                try {
                    rin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
				resFile.close();
				if(resFile.getErrorCount() == 0) {
					resFile.deleteErrorFile();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return resFile;
    }
    
    public static TxtXlsResultFile checkExcelFileAuto(File file, CheckFormat iCheckFormat)
			throws FormatException, WriteException{
    	return checkExcelFileAuto(file, iCheckFormat, null);
    }
    
    public static TxtXlsResultFile checkExcelFileAuto(File file, CheckFormat iCheckFormat,
    		ArrayList<DynamicField> checkFields) 
			throws FormatException, WriteException{
    	if(iCheckFormat != null){	
    		try {
    			try {
    				if(checkFields != null) {
    					iCheckFormat.checkLine(new Cell[0], checkFields);
    				} else {
    					iCheckFormat.checkLine(new Cell[0]);
    				}
    			} catch(FormatException e) {
    			}
    			return checkExcelFile(file, iCheckFormat, checkFields);
    		} catch(UnsupportedOperationException e) {
    			return checkExcelFileByTxtType(file, iCheckFormat, checkFields);
    		}
    	}
    	return null;
    }
    
    public static TxtXlsResultFile checkExcelFile(File file, CheckFormat iCheckFormat) throws FormatException, WriteException{
    	return checkExcelFile(file, iCheckFormat, null);
    }
    
    /**
     * 检查Excel文件
     * @param file
     * @param iCheckFormat
     * @param checkFields
     * @return
     * @throws FormatException
     * @throws WriteException 
     */
    public static TxtXlsResultFile checkExcelFile(File file, CheckFormat iCheckFormat, 
    		ArrayList<DynamicField> checkFields) 
    		throws FormatException, WriteException{
    	return checkExcelFile(file, iCheckFormat, checkFields, "xls");
    }
    
    public static TxtXlsResultFile checkExcelFileByTxtType(File file, CheckFormat iCheckFormat) 
    		throws FormatException{
    	return checkExcelFileByTxtType(file, iCheckFormat);
    }
    
    public static TxtXlsResultFile checkExcelFileByTxtType(File file, CheckFormat iCheckFormat, 
    		ArrayList<DynamicField> checkFields) 
    		throws FormatException, WriteException{
    	return checkExcelFile(file, iCheckFormat, checkFields, "txt");
    }
    
    public static TxtXlsResultFile checkExcelFile(File file, CheckFormat iCheckFormat, 
    		ArrayList<DynamicField> checkFields, String checkType) 
            throws FormatException, WriteException{
        if ( iCheckFormat == null ) {
            return null;
        }
        int count = 0;
        int currentRow = iCheckFormat.getBeginIndex();
        String errName = file.getAbsolutePath().replaceFirst(".xls", "_format.xls");
        TxtXlsResultFile resFile = new TxtXlsResultFile();
        resFile.initialize(errName);
        Workbook wwb = null;
        try {
        	resFile.openFile();
            wwb = Workbook.getWorkbook(new BufferedInputStream(new FileInputStream(file)));
            Sheet sheet = wwb.getSheet(0);
            for(int i = iCheckFormat.getBeginIndex() - 1; i < sheet.getRows(); ++i, ++currentRow) {
            	iCheckFormat.setCurrentRow(currentRow);
            	Cell[] cells = sheet.getRow(i);
            	if(JxlUtil.isEmptyRow(cells)) {
            		continue;
            	}
            	try {
            		if(checkType.equals("xls")) {
            			if(null == checkFields) {
            				iCheckFormat.checkLine(cells);
            			} else {
            				iCheckFormat.checkLine(cells, checkFields);
            			}
            		} else if(checkType.equals("txt")){
            			String line = JxlUtil.toStringLine(cells);
            			if(null == checkFields) {
            				iCheckFormat.checkLine(line);
            			} else {
            				iCheckFormat.checkLine(line, checkFields);
            			}
            		} else {
            			if(null == checkFields) {
            				iCheckFormat.checkLine(cells);
            			} else {
            				iCheckFormat.checkLine(cells, checkFields);
            			}
            		}
            		count++;
            		iCheckFormat.setPassCount(count);
            	} catch (FormatException ex) {
            		String errInfo = "第" + currentRow + "行：" + ex.getMessage();
                    Cell[] errCell = new Cell[cells.length + 1];
                    for(int j = 0; j < cells.length; ++j) {
                    	errCell[j] = cells[j];
                    }
                    Label label = new Label(cells.length, cells[0].getRow(), errInfo);
                    errCell[cells.length] = label;
                    resFile.writeErrorRecord(errCell, resFile.getErrorCount());
                }
            }
            sheet = null;
        } catch (FileNotFoundException fnfe) {
            throw new FormatException("找不到文件：" + fnfe.getMessage());
        } catch (BiffException be) {
            throw new FormatException(be.getMessage());
        }  catch (WriteException e) {
        	throw new FormatException(e.getMessage());
		} catch (IOException e) {
			throw new FormatException(e.getMessage());
		} finally {
            if(wwb != null) {
                wwb.close();
            }
            try {
				resFile.close();
				if(resFile.getErrorCount() == 0) {
					resFile.deleteErrorFile();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return resFile;
    }
}
