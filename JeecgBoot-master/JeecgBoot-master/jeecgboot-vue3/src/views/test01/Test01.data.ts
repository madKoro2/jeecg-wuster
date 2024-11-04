import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
  {
    title: '测试主键',
    align: "center",
    dataIndex: 'testid'
  },
  {
    title: '测试内容',
    align: "center",
    dataIndex: 'testcontent'
  },
];

// 高级查询数据
export const superQuerySchema = {
  testid: {title: '测试主键',order: 0,view: 'text', type: 'string',},
  testcontent: {title: '测试内容',order: 1,view: 'text', type: 'string',},
};
